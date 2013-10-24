package de.iteratec.stechuhr.projectile.test

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.openqa.selenium.By

import de.iteratec.stechuhr.projectile.ProjectileBooking
import de.iteratec.stechuhr.projectile.ProjectileUser

class ProjectileUserTest {

	ProjectileUser user

	@Before
	public void setUp() {
		user = new ProjectileUser()
	}

	@Test
	public void testLogin() {

		user.openProjectileAndLogIn "thhi", "!start01"

		assertTrue user.driver.isElementVisible(By.cssSelector("td[bgcolor='#dddddd']"))
	}

	@Test
	public void testGetProjectOptionNames() {

		assertTrue user.getProjectOptionNames("thhi", "!start01").size() > 0
	}

	@Test
	public void testMakeBooking() {

		ProjectileBooking booking = new ProjectileBooking(projectOptionName: "_INT-Temporär-2013 - PARKPOSITION", startTime: "10:30", endTime: "11:35", comment: "blablubb")

		user.makeBooking "thhi", "!start01", booking
	}
}
