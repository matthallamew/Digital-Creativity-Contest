package dcc

class Submission {
	String title
	String author
	String emailAddr
	String steps
	String applications
	String goals
	String link
	String category
	Date dateCreated
	Date lastUpdated
	
	static transients =['grandTotal']
	
	static hasMany = [ranks:Rank,judges:Judge]

    static constraints = {
		category blank:false
		title blank:false
		author nullable:true
		emailAddr blank:true,email:true
		link blank:false
		steps nullable:true
		applications nullable:true
		goals nullable:true
    }
	String toString(){title}

	Integer getGrandTotal() {
		def total = 0 
		this.ranks.total.each{ tot ->
			total += tot.toInteger()
		}
		return total
	}
}