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
	Integer grandTotal = 0
	Boolean winner = false
	Set ranks = []
	Set judges = []
	
	static hasMany = [ranks:Rank,judges:Judge]

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

	def afterInsert() {
		updateGrandTotal()
	}
	def beforeUpdate() {
		updateGrandTotal()
	}
	protected boolean updateGrandTotal(){
		def total = 0
		this.ranks.total.each{ tot ->
			total += tot.toInteger()
		}
		this.grandTotal = total
		if(this.hasErrors() || !this.save()){
			return false
		}
		return true
	}

	protected boolean updateGrandTotalAll(){
		def lst = Submission.list()
		lst.each {
			if(!it.updateGrandTotal()) {
				return false
			}
			return true
		}
	}
	String toString(){title}
}