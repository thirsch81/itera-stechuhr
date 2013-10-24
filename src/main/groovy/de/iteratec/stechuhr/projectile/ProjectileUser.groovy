package de.iteratec.stechuhr.projectile

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.htmlunit.HtmlUnitDriver

import de.thokari.webdriver.GroovyWebDriver

class ProjectileUser {

	static final String PROJECTILE_URL = "https://project.iteratec.de/projectile/start"

	static final By TABLE_ROW_PROJECT = By.xpath "//table/tbody/tr/td[contains(text(), 'Hauptprojekt')]/../../tr[position() > 1]"

	static final By INPUT_USERNAME_LOCATOR = By.cssSelector "input[name='login']"
	static final By INPUT_PASSWORD_LOCATOR = By.cssSelector "input[name='password']"
	static final By BUTTON_LOGIN_LOCATOR = By.cssSelector "input[name='external.loginOK']"
	static final By BUTTON_TIME_TRACKER_LOCATOR = By.cssSelector "input[alt='Zeiterfassung']"

	GroovyWebDriver driver

	public ProjectileUser() {
		driver = new GroovyWebDriver(driver: new HtmlUnitDriver(true))
		driver.setImplicitWait 10000
	}

	public void openProjectileAndLogIn(String username, String password) {
		driver.open PROJECTILE_URL
		driver.type INPUT_USERNAME_LOCATOR, username
		driver.type INPUT_PASSWORD_LOCATOR, password
		driver.click BUTTON_LOGIN_LOCATOR
	}

	public void openTimeTracker() {
		driver.click BUTTON_TIME_TRACKER_LOCATOR
	}

	public List<ProjectileProject> getProjects() {
		List<WebElement> tableRows = driver.findElements TABLE_ROW_PROJECT
		List<ProjectileProject> result = []
		tableRows.each { row ->
			List<WebElement> cells = row.findElements By.cssSelector("td")
			result.add new ProjectileProject(
					mainProject: cells[0].text,
					id: cells[1].text,
					name: cells[2].text,
					workPackage: cells[3].text,
					client: cells[4].text,
					deadline: cells[5].text,
					plannedEffort: cells[6].text,
					currentEffort: cells[7].text
					)
		}
		result
	}

	public List<String> getProjectOptionNames() {
		getProjects()*.getOptionName()
	}
}
