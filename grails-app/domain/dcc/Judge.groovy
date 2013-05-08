package dcc

class Judge extends SecUser {
	Submission submission
	
	static hasMany = [ranks:Rank]
	
    static constraints = {
		submission nullable:true
    }

	String toString(){"$firstName $lastName"}
}