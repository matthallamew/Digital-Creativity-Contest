package dcc

class Judge extends SecUser {
	
	static hasMany = [ranks:Rank]
	
    static constraints = {
    }
	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
	String toString(){"$firstName $lastName"}
}