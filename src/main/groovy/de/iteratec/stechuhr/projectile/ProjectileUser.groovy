package de.iteratec.stechuhr.projectile

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.support.ui.Select

import de.thokari.webdriver.GroovyWebDriver

class ProjectileUser {

	static final String PROJECTILE_URL = "https://project.iteratec.de/projectile/start"

	static final By INPUT_USERNAME = By.cssSelector "input[name='login']"
	static final By INPUT_PASSWORD = By.cssSelector "input[name='password']"
	static final By BUTTON_LOGIN = By.cssSelector "input[name='external.loginOK']"

	static final By BUTTON_TIME_TRACKER = By.cssSelector "input[alt='Zeiterfassung']"
	static final By BUTTON_SET_CURRENT_DAY = By.cssSelector "input[alt='Aktuellen Tag setzen']"

	static final By TABLE_ROW_PROJECT = By.xpath "//table/tbody/tr/td[contains(text(), 'Hauptprojekt')]/../../tr[position() > 1]"

	static final By TABLE_ROW_FIRST_EMPTY_BOOKING = By.xpath "(//table/tbody/tr/td[contains(text(), 'Aktiver Tag')]/../..//input[contains(@name, 'NewTime')]/../../../../../..)[1]"
	static final By BUTTON_MORE_BOOKING_ROWS = By.cssSelector "input[alt='mehr']"
	static final By BUTTON_SAVE = By.cssSelector "input[alt='Änderungen speichern']"


	GroovyWebDriver driver

	public ProjectileUser() {
		driver = new GroovyWebDriver(driver: new HtmlUnitDriver(true))
		driver.setImplicitWait 3500
	}

	private void openProjectileAndLogIn(String username, String password) {
		driver.open PROJECTILE_URL
		driver.type INPUT_USERNAME, username
		driver.type INPUT_PASSWORD, password
		driver.click BUTTON_LOGIN
	}

	private void openTimeTracker() {
		driver.click BUTTON_TIME_TRACKER
	}

	private void setCurrentDay() {
		driver.click BUTTON_SET_CURRENT_DAY
	}

	private void clickMoreBookingRows() {
		driver.click BUTTON_MORE_BOOKING_ROWS
	}

	private WebElement findFirstUnusedBookingRow() {
		clickMoreBookingRows()
		driver.findElement TABLE_ROW_FIRST_EMPTY_BOOKING
	}

	private void clickSave() {
		driver.click BUTTON_SAVE
	}

	private void inputBooking(WebElement bookingRow, ProjectileBooking booking) {
		List<WebElement> inputs = bookingRow.findElements By.cssSelector("input")
		inputs[0].sendKeys booking.startTime
		inputs[1].sendKeys booking.endTime
		Select select = new Select(bookingRow.findElement(By.cssSelector("select")))
		select.selectByVisibleText booking.projectOptionName
		inputs[3].sendKeys booking.comment
	}

	private List<ProjectileProject> getProjects() {
		List<WebElement> tableRows = driver.findElements TABLE_ROW_PROJECT
		List<ProjectileProject> result = []
		tableRows.each { row ->
			List<String> cells = row.findElements(By.cssSelector("td")).collect { it.text }
			result.add new ProjectileProject(
					mainProject: cells[0],
					id: cells[2],
					name: cells[4],
					workPackage: cells[6],
					client: cells[8],
					deadline: cells[10],
					plannedEffort: cells[12],
					currentEffort: cells[14]
					)
		}
		result
	}

	public List<String> getProjectOptionNames(String username, String password) {
		openProjectileAndLogIn username, password
		openTimeTracker()
		getProjects()*.optionName
	}

	public void makeBooking(String username, String password, ProjectileBooking booking) {
		openProjectileAndLogIn username, password
		openTimeTracker()
		setCurrentDay()
		inputBooking findFirstUnusedBookingRow(), booking
		clickSave()
	}
}
