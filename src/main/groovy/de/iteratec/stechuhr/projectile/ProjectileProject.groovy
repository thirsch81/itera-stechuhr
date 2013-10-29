package de.iteratec.stechuhr.projectile

class ProjectileProject {

	String mainProject
	String id
	String name
	String workPackage
	String client
	String deadline

	String plannedEffort
	String currentEffort

	public String getOptionName() {
		"$name - $workPackage"
	}

	@Override
	public String toString() {
		"ProjectileProject [mainProject=$mainProject, id=$id, name=$name, workPackage=$workPackage, client=$client, deadline=$deadline, plannedEffort=$plannedEffort, currentEffort=$currentEffort]"
	}
}
