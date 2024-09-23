package com.dangtm.movie.service;

import com.dangtm.movie.configuration.PredefinedAudioType;
import com.dangtm.movie.constrant.PredefinedMovieStatus;
import com.dangtm.movie.entity.*;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CrawlService {

    String URL = "https://www.cgv.vn/default/";

    @NonFinal
    @Value("${chrome.user.data.path}")
    String CHROME_USER_DATA_PATH;
    @NonFinal
    @Value("${chrome.driver.path}")
    String CHROME_PATH;
    @NonFinal
    WebDriver driver;
    @NonFinal
    WebDriverWait wait;
    @NonFinal
    JavascriptExecutor js;
    @NonFinal
    boolean isSwitch = false;

    MovieRepository movieRepository;
    CinemaRepository cinemaRepository;
    CityRepository cityRepository;
    CinemaImageRepository cinemaImageRepository;
    AudioRepository audioRepository;
    ShowRepository showRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public void crawl(String type, Optional<Integer> crawDate) {
        System.setProperty("webdriver.chrome.driver", CHROME_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--user-data-dir=" + CHROME_USER_DATA_PATH);

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        driver.manage().window().maximize();
        try {
            if (type.equals("movie")) {
                crawlMovie(driver);
            } else if (type.equals("show")) {
                crawlCityAndCinema(driver, crawDate);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.CRAWL_FAILED);
        } finally {
            driver.quit();
        }
    }

    private void crawlCityAndCinema(WebDriver driver, Optional<Integer> crawDate) {

        log.info("Start crawling city, cinema and show");

        List<City> cities = new ArrayList<>();
        List<Cinema> cinemas = new ArrayList<>();
        List<Show> shows = new ArrayList<>();
        driver.get(URL);

        var banner = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cboxContent")));
        WebElement closeBanner = banner.findElement(By.id("cboxClose"));
        closeBanner.click();

        // find theatre nav
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"nav\"]/ol/li[2]"))).click();

        // navigate to theatre page
        driver.findElement(By.xpath("//*[@id=\"nav\"]/ol/li[2]/ul/li[1]")).click();

        var cityUl = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"top\"]/body/div[3]/div/div[3]/div/div/div/div[1]/div/div[1]/div[2]/div/div[2]/ul")));

        var liList = cityUl.findElements(By.tagName("li"));

        for (int i = 1; i <= liList.size(); i++) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-mask")));
            var cityEl = driver.findElement(
                    By.xpath("//*[@id=\"top\"]/body/div[3]/div/div[3]/div/div/div/div[1]/div/div[1]/div[2]/div/div[2]/ul/li[" + i + "]")
            );
            log.info("Access city: {}", cityEl.getText());
            js = (JavascriptExecutor) driver;
            City city = City.builder()
                    .name(cityEl.getText())
                    .build();

            if (cityRepository.findByName(city.getName()).isEmpty()) {
                cities.add(city);
            } else {
                city = cityRepository.findByName(city.getName()).orElse(null);
            }

            String cityId = cityEl.findElement(By.tagName("span")).getAttribute("id");

            cityEl.click();
            var cinemaUl = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//*[@id=\"top\"]/body/div[3]/div/div[3]/div/div/div/div[1]/div/div[1]/div[2]/div/div[3]/ul"
            )));
            var cinemaList = cinemaUl.findElements(By.className(cityId));

            for (int m = 0; m < cinemaList.size(); m++) {
                if (isSwitch) {
                    cinemaList = cinemaUl.findElements(By.className(cityId));
                    isSwitch = false;
                }
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-mask")));
                var cinemaEl = cinemaList.get(m);
                var span = cinemaEl.findElement(By.tagName("span"));
                span.click();

                Cinema cinema = crawlTheaterInfo(city);
                log.info("Access cinema: {}", cinema.getName());

                if (cinemaRepository.findByName(cinema.getName()).isEmpty()) {
                    cinemas.add(cinema);
                } else {
                    cinema = cinemaRepository.findByName(cinema.getName()).orElse(null);
                }

                var dateUl = driver.findElement(By.xpath("//*[@id=\"first\"]/div/ul"));

                var dateElements = dateUl.findElements(By.tagName("li"));

                int start = crawDate.orElse(1);
                int end = start + 1;
                for (int l = start; l < end; l++) {
                    var dateEl = dateElements.get(l);
                    LocalDate showDate = getShowDate(dateEl);
                    log.info("Access show at: {}", showDate);
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-mask")));
                    dateEl.click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-mask")));
                    var showListWrapperEl = driver.findElement(By.className("tabs-cgv-showtimes"));
                    var content = showListWrapperEl.findElements(By.tagName("div"));
                    if (content.isEmpty()) {
                        log.warn("Date {} has no show", showDate);
                    } else {
                        List<WebElement> showListEl = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("film-list")));

                        for (int k = 1; k <= showListEl.size(); k++) {
                            var firmListEl = showListEl.get(k - 1);

                            Movie showMovie = getShowMovie(firmListEl);

                            var showListWrapper = firmListEl.findElement(By.className("film-right"));

                            var filmScreenList = showListWrapper.findElements(By.className("film-screen"));

                            for (int j = 1; j <= filmScreenList.size(); j++) {

                                var filmScreen = filmScreenList.get(j - 1);

                                String audioType = getAudioType(filmScreen.getText().trim());

                                Audio audio = audioRepository.findAudioByType(audioType);

                                var showTimeUl = driver.findElement(By.xpath(
                                        "//*[@id=\"collateral-tabs\"]/dd[1]/div/div[2]/div[" + k + "]/div[3]/div[" + j + "]/ul"
                                ));

                                var showTimeList = showTimeUl.findElements(By.tagName("li"));

                                for (WebElement showTimeLi : showTimeList) {

                                    LocalTime startTime = getStartTime(showTimeLi);

                                    log.info("Access show at {} {}", startTime, showDate);

                                    Show show = showRepository.findShowByDateAndStartTimeAndCinema_CinemaIdAndMovie_Id(showDate, startTime, cinema.getCinemaId(), showMovie.getId()).orElse(null);

                                    if (show == null) {
                                        show = Show.builder()
                                                .date(showDate)
                                                .startTime(startTime)
                                                .cinema(cinema)
                                                .movie(showMovie)
                                                .price(75000)
                                                .audio(audio)
                                                .build();

                                        shows.add(show);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!cities.isEmpty()) {
            cityRepository.saveAll(cities);
        }
        if (!cinemas.isEmpty()) {
            cinemaRepository.saveAll(cinemas);
            cinemas.forEach(cinema -> cinemaImageRepository.saveAll(cinema.getImages()));
        }
        showRepository.saveAll(shows);
    }

    private Cinema crawlTheaterInfo(City city) {

        var nameEl = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"top\"]/body/div[3]/div/div[3]/div/div/div/div[1]/div/div[2]/div[1]/div[2]")
        ));

        String cinemaName = nameEl.getText().trim();

        var addressEl = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("theater-address")
        ));

        String cinemaAddress = addressEl.getText().trim();

        var faxEl = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"top\"]/body/div[3]/div/div[3]/div/div/div/div[1]/div/div[2]/div[2]/div[2]/div[1]/div/div[2]/div")
        ));

        String cinemaFax = faxEl.getText().trim();

        var hotlineEl = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"top\"]/body/div[3]/div/div[3]/div/div/div/div[1]/div/div[2]/div[2]/div[2]/div[1]/div/div[3]/div")
        ));

        String cinemaHotline = hotlineEl.getText().trim();

        var imageSlideEl = driver.findElement(
                By.className("slideshow")
        );

        var imageEls = imageSlideEl.findElements(By.tagName("img"));

        Cinema cinema = Cinema.builder()
                .name(cinemaName)
                .address(cinemaAddress)
                .fax(cinemaFax)
                .hotline(cinemaHotline)
                .city(city)
                .build();

        Set<CinemaImage> images = imageEls.stream().map(
                imageEl -> CinemaImage.builder()
                        .imageUrl(imageEl.getAttribute("src").trim())
                        .cinema(cinema)
                        .build()
        ).collect(Collectors.toSet());

        cinema.setImages(images);

        return cinema;
    }

    private List<Movie> crawlMovie(WebDriver driver) {
        List<Movie> movies = new ArrayList<>();
        try {
            driver.get(URL);
            var banner = wait.until(ExpectedConditions.elementToBeClickable(By.id("cboxContent")));
            WebElement closeBanner = banner.findElement(By.id("cboxClose"));
            closeBanner.click();

            var movieTypeListEl = driver.findElement(By.xpath("//*[@id=\"nav\"]/ol/li[1]/ul"));

            var movieTypeList = movieTypeListEl.findElements(By.tagName("a"));

            for (int i = 1; i <= movieTypeList.size(); i++) {
                var movieNav = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"nav\"]/ol/li[1]")));
                movieNav.click();
                var movieTypeEl = driver.findElement(By.xpath("//*[@id=\"nav\"]/ol/li[1]/ul/li[" + i + "]/a"));
                String movieType = movieTypeEl.getText();
                String status = movieType.equalsIgnoreCase("Phim Đang Chiếu") ? PredefinedMovieStatus.SHOWING : PredefinedMovieStatus.COMING_SOON;
                movieTypeEl.click();
                String movieURL = driver.getCurrentUrl();
                var movieListEl = driver.findElement(By.xpath("//*[@id=\"top\"]/body/div[3]/div/div[3]/div[2]/div/div[2]/ul"));

                var movieList = movieListEl.findElements(By.className("film-lists"));
                for (int j = 1; j <= movieList.size(); j++) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("//*[@id=\"top\"]/body/div[3]/div/div[3]/div[2]/div/div[2]/ul/li[").append(j).append("]");
                    var movieEl = driver.findElement(By.xpath(builder.toString()));
                    js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].scrollIntoView(true);", movieEl);

                    movieEl.click();

                    Movie movie = crawlMovieInfo(status);

                    Movie existingMovie = movieRepository.findByTitle(movie.getTitle()).orElse(null);

                    if (existingMovie == null) {
                        movies.add(movie);
                    } else {
                        existingMovie.setTitle(movie.getTitle());
                        existingMovie.setDescription(movie.getDescription());
                        existingMovie.setReleaseDate(movie.getReleaseDate());
                        existingMovie.setActors(movie.getActors());
                        existingMovie.setDirector(movie.getDirector());
                        existingMovie.setGenre(movie.getGenre());
                        existingMovie.setLanguage(movie.getLanguage());
                        existingMovie.setDuration(movie.getDuration());
                        existingMovie.setRated(movie.getRated());
                        existingMovie.setImage(movie.getImage());
                        existingMovie.setTrailer(movie.getTrailer());
                        existingMovie.setStatus(movie.getStatus());
                        movies.add(existingMovie);
                    }

                    driver.get(movieURL);
                }
                driver.get(URL);
            }

        } catch (NoSuchElementException e) {
            throw new RuntimeException(e.getMessage());
        }

        return movieRepository.saveAll(movies);
    }

    private Movie crawlMovieInfo(String status) {

        var imageEl = findElementSafely(driver, By.xpath("//*[@id=\"image-main\"]"));
        StringBuilder image = new StringBuilder();
        image.append(Objects.isNull(imageEl) ? "" : imageEl.getAttribute("src").trim());

        var titleEL = findElementSafely(driver, By.className("product-name"));
        StringBuilder title = new StringBuilder();
        title.append(Objects.isNull(titleEL) ? "" : titleEL.getText().trim());

        var directorEl = findElementSafely(driver, By.xpath("//*[@id=\"top\"]/body/div[3]/div/div[3]/div[2]/div/div[2]/div[1]/div[4]/div[2]/div"));
        StringBuilder director = new StringBuilder();
        director.append(Objects.isNull(directorEl) ? "" : directorEl.getText().trim());

        var actorEl = findElementSafely(driver, By.xpath("//*[@id=\"top\"]/body/div[3]/div/div[3]/div[2]/div/div[2]/div[1]/div[4]/div[3]/div"));
        StringBuilder actor = new StringBuilder();
        actor.append(Objects.isNull(actorEl) ? "" : actorEl.getText().trim());

        var genreEl = findElementSafely(driver, By.xpath("//*[@id=\"top\"]/body/div[3]/div/div[3]/div[2]/div/div[2]/div[1]/div[4]/div[4]/div"));
        StringBuilder genre = new StringBuilder();
        genre.append(Objects.isNull(genreEl) ? "" : genreEl.getText().trim());

        var releaseEl = findElementSafely(driver, By.xpath("//*[@id=\"top\"]/body/div[3]/div/div[3]/div[2]/div/div[2]/div[1]/div[4]/div[5]/div"));
        StringBuilder release = new StringBuilder();
        release.append(Objects.isNull(releaseEl) ? "" : releaseEl.getText().trim());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate releaseDate = release.toString().isEmpty() ? null : LocalDate.parse(release.toString(), formatter);

        var durationEl = findElementSafely(driver, By.xpath("//*[@id=\"top\"]/body/div[3]/div/div[3]/div[2]/div/div[2]/div[1]/div[4]/div[6]/div"));
        StringBuilder duration = new StringBuilder();
        duration.append(Objects.isNull(durationEl) ? "" : durationEl.getText().trim());

        var languageEl = findElementSafely(driver, By.xpath("//*[@id=\"top\"]/body/div[3]/div/div[3]/div[2]/div/div[2]/div[1]/div[4]/div[7]/div"));
        StringBuilder language = new StringBuilder();
        language.append(Objects.isNull(languageEl) ? "" : languageEl.getText().trim());

        var ratedEl = findElementSafely(driver, By.xpath("//*[@id=\"top\"]/body/div[3]/div/div[3]/div[2]/div/div[2]/div[1]/div[4]/div[8]/div"));
        String ratedList = Objects.isNull(ratedEl) ? "" : ratedEl.getText().trim();
        StringBuilder rated = new StringBuilder();
        rated.append(ratedList.split(" - ")[0]);

        var descriptionEl = findElementSafely(driver, By.xpath("//*[@id=\"collateral-tabs\"]/dd[1]/div/div"));
        StringBuilder description = new StringBuilder();
        description.append(Objects.isNull(descriptionEl) ? "" : descriptionEl.getText().trim());

        var trailerEL = findElementSafely(driver, By.xpath("//*[@id=\"collateral-tabs\"]/dd[2]/div/div/div/iframe"));
        StringBuilder trailer = new StringBuilder();
        trailer.append(Objects.isNull(trailerEL) ? "" : trailerEL.getAttribute("src").trim());

        return Movie.builder()
                .title(title.toString())
                .genre(genre.toString())
                .actors(actor.toString())
                .description(description.toString())
                .director(director.toString())
                .duration(duration.toString())
                .language(language.toString())
                .rated(rated.toString())
                .trailer(trailer.toString())
                .status(status)
                .image(image.toString())
                .releaseDate(releaseDate)
                .build();
    }

    private WebElement findElementSafely(WebDriver driver, By locator) {
        try {
            return driver.findElement(locator);
        } catch (NoSuchElementException e) {
            return null; // Return null if element is not found
        }
    }

    private String getAudioType(String screenType) {
        // Create a map to associate screen type keywords with PredefinedAudioType values
        Map<String, String> audioTypeMap = new HashMap<>();
        audioTypeMap.put(PredefinedAudioType.LONG_TIENG_VIET, PredefinedAudioType.LONG_TIENG_VIET);
        audioTypeMap.put(PredefinedAudioType.PHU_DE_ANH, PredefinedAudioType.PHU_DE_ANH);
        audioTypeMap.put(PredefinedAudioType.PHU_DE_ANH_VIET, PredefinedAudioType.PHU_DE_ANH_VIET);

        // Iterate over the map and check if the screenType contains any of the keys
        for (Map.Entry<String, String> entry : audioTypeMap.entrySet()) {
            if (screenType.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // Default return value if no match is found
        return PredefinedAudioType.PHU_DE_VIET;
    }

    private LocalDate getShowDate(WebElement dateEl) {
        String dateElId = dateEl.getAttribute("id").trim();

        String dateString = dateElId.substring(3);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return LocalDate.parse(dateString, formatter);
    }

    private LocalTime getStartTime(WebElement showTimeLi) {
        String showTime = showTimeLi.getText().trim()
                .replace(" PM", "")
                .replace(" AM", "");

        String[] timeParts = showTime.split(":");
        int hour = Integer.parseInt(timeParts[0]);

        if (hour >= 24) {
            hour -= 24;
        }

        showTime = hour + ":" + timeParts[1];

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

        return LocalTime.parse(showTime, timeFormatter);
    }

    private Movie getShowMovie(WebElement firmListEl) {
        var firmLabelEl = firmListEl.findElement(By.className("film-label"));
        var firmTitleEl = firmLabelEl.findElement(By.tagName("a"));
        String movieUrl = firmTitleEl.getAttribute("href");
        String movieTitle = firmTitleEl.getAttribute("title").trim();

        Movie showMovie = movieRepository.findByTitle(movieTitle).orElse(null);

        // update database if the movie do not in database
        if (showMovie == null) {
            isSwitch = true;
            String originalWindow = driver.getWindowHandle();

            // Open a new tab and switch to it
            driver.switchTo().newWindow(WindowType.TAB);
            driver.get(movieUrl);

            // Perform the crawling in the new tab
            showMovie = crawlMovieInfo(PredefinedMovieStatus.SHOWING);
            showMovie = movieRepository.save(showMovie);

            // Close the new tab and switch back to the original window
            driver.close();
            driver.switchTo().window(originalWindow);
        }

        return showMovie;
    }

}
