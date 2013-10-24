package de.iteratec.stechuhr.projectile.test

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.openqa.selenium.By

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
	public void testGetPojects() {
		
		user.openProjectileAndLogIn "thhi", "!start01"
				
		user.getProjectList()
	}
	
}
