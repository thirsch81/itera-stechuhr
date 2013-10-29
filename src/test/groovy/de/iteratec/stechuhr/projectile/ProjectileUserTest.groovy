package de.iteratec.stechuhr.projectile

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.openqa.selenium.By

class ProjectileUserTest {

	ProjectileUser user

	@Before
	public void setUp() {
		user = new ProjectileUser()
	}

	@Test
	public void testLogin() {

		user.openProjectileAndLogIn "thhi", "dontwork"

		assertTrue user.driver.isElementVisible(By.cssSelector("td[bgcolor='#dddddd']"))
	}

	@Test
	public void testGetProjectOptionNames() {

		assertTrue user.getProjectOptionNames("thhi", "dontwork").size() > 0
	}

	@Test
	public void testMakeBooking() {

		ProjectileBooking booking = new ProjectileBooking(projectOptionName: "_INT-Temporär-2013 - PARKPOSITION", startTime: "10:30", endTime: "11:35", comment: "blablubb")

		user.makeBooking "thhi", "dontwork", booking
	}
}
