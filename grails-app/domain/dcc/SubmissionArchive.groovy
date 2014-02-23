package dcc

class SubmissionArchive {
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
	Integer grandTotal = 0
	Integer contestYear = 0
	Boolean winner = false
	
    static constraints = {
		category blank:false
		title blank:false
		author nullable:true
		emailAddr blank:true,email:true
		link blank:false
		steps nullable:true,maxSize: 1000
		applications nullable:true,maxSize: 1000
		goals nullable:true,maxSize: 1000
    }
	String toString(){title}
}