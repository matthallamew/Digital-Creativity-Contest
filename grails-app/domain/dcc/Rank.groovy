package dcc

class Rank {
	int skill
	String skillComment
	int creativity
	String creativityComment
	int aesthetic
	String aestheticComment
	int purpose
	String purposeComment
	int total
	Date dateCreated
	Date lastUpdated
	
	static belongsTo = [judges:Judge,submissions:Submission]
	
    static constraints = {
		skill range:1..6
		skillComment nullable:true
		creativity  range:1..6
		creativityComment nullable:true
		aesthetic  range:1..6
		aestheticComment nullable:true
		purpose  range:1..6
		purposeComment nullable:true
		total range:0..24
	}
}
