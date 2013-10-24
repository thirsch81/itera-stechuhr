package de.iteratec.stechuhr.projectile

class ProjectileProject {

	String mainProject
	String id
	String name
	String workPackage // -> LEO
	String client
	String deadline
	String plannedEffort
	String currentEffort

	public String getOptionName() {
		"$name - $workPackage"
	}
}
