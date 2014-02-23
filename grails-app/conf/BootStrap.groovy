import dcc.Judge
import dcc.Rank
import dcc.SecRole
import dcc.SecUser
import dcc.Submission
import dcc.SecUserSecRole

class BootStrap {

    def init = { servletContext ->
		(1..25).each{
			new Submission(category:"Whatever",title:"My Subbmission $it",author:"Author $it",link:"http://youtu.be/5L28TM48bF0",emailAddr:"email1@domain.com").save(flush:true)
		}

		Judge j = new Judge(username:'peterj',password:'a',firstName:'PeterJ',lastName:'Griffon',enabled:true).save(flush:true)
		//Judge j = Judge.findByUsername('peterj')
		Submission s1 = Submission.get(1)
		new Rank(skill:6,creativity:6,aesthetic:6,purpose:6,total:24,submissions:s1,judges:j).save(flush:true)
		new Rank(skill:6,creativity:6,aesthetic:6,purpose:6,total:24,submissions:s1,judges:j).save(flush:true)

		def adminRole = SecRole.findByAuthority("ROLE_ADMIN") ?: new SecRole(authority:"ROLE_ADMIN").save(flush:true)
		def judgeRole = SecRole.findByAuthority("ROLE_JUDGE") ?: new SecRole(authority:"ROLE_JUDGE").save(flush:true)

		def admin = new SecUser(username:'tsmith',password:'pssdubdd',firstName:'Todd',lastName:'Smith',enabled:true).save(flush:true)

		SecUserSecRole.create admin,adminRole
		SecUserSecRole.create j, judgeRole
	}
    def destroy = {
    }
}
