package dcc

import org.junit.*
import grails.test.mixin.*

@TestFor(AdminController)
@Mock([Judge,Submission,Rank,SecUser,SecRole,SecUserSecRole])
class AdminControllerTests {

	@Before
	void setUp() {
		controller.adminService = new AdminService()
		Judge.metaClass.encodePassword = {'a'}
		SecUser.metaClass.encodePassword = {'a'}
	}

	def populateValidParams(params) {
		assert params != null
		params["category"] = "Cool"
		params["title"] = "Great Project"
		params["author"] = "Matt Dubb"
		params["emailAddr"] = "mdub@email.com"
		params["link"] = "https://coollinks.com"
		params["skill"] = 6
		params["creativity"] = 6
		params["aesthetic"] = 6
		params["purpose"] = 6
		params["total"] = 24
		params["authority"] = "ROLE_ADMIN"
		params["username"]='peterj'
		params["password"] = 'a'
		params["firstName"]='PeterJ'
		params["lastName"]='Griffon'
		params["enabled"]=true
	}
	
    void testNoSubmissionsIndex() {
		def model = controller.index()
		
		assert model.submissionsText.trim() == "0 Submissions are"
    }
	
    void test1SubmissionIndex() {
        populateValidParams(params)
        Submission submission = new Submission(params).save(flush:true)
		def model = controller.index()
		
		assert model.submissionsText.trim() == "1 Submission is"
    }
	
    void testMoreThan1SubmissionIndex() {
        populateValidParams(params)
        Submission submission = new Submission(params).save(flush:true)
        Submission submission2 = new Submission(params).save(flush:true)
		def model = controller.index()
		
		assert model.submissionsText.trim() == "2 Submissions are"
    }
	
	void testNoSubmissionsShowScoring(){
		controller.showScoring()
		
		assert flash.message != null
		assert response.redirectedUrl == "/admin/index"
	}
	
	void test1SubmissionShowScoring() {
        populateValidParams(params)
        Submission submission = new Submission(params).save(flush:true)
		def model = controller.showScoring()
		
		assert model.submissionListTotal == 1
		assert Submission.count() == 1
	}
	
	void testMoreThan1SubmissionShowScoring() {
		populateValidParams(params)
		Submission submission = new Submission(params).save(flush:true)
		Submission submission2 = new Submission(params).save(flush:true)
		def model = controller.showScoring()

		assert model.submissionListTotal == 2
		assert model.categoryList == ["Cool"]
		assert Submission.count() == 2
	}
	
	void test1SubmissionParamsCategoryNullShowScoring() {
		populateValidParams(params)
		Submission submission = new Submission(params).save(flush:true)
		params.category = null
		def model = controller.showScoring()
		
		assert model.submissionList == null
		assert model.categoryList == ["Cool"]
		assert Submission.count() == 1
	}
	
	void testNoSubmissionShowSubmission() {
		controller.showSubmission()
		
		assert flash.message != null
		assert response.redirectedUrl == "/admin/showScoring"
	}

	void testValidSubmissionShowSubmission() {
		populateValidParams(params)
		Submission submission = new Submission(params).save(flush:true)
		params.id = submission.id
		def model = controller.showSubmission()
		
		assert flash.message == null
		assert Submission.count() == 1
		assert model.submissionInstance == submission
	}
	
	void testInvalidUpdateWinnerStatus() {
		controller.updateWinnerStatus()
		
		assert response.redirectedUrl =="/admin/showSubmission"
		assert flash.message != null
	}
	
	void testValidSubmissionUpdateWinnerStatus() {
		populateValidParams(params)
		Submission submission = new Submission(params).save(flush:true)
		params.id = submission.id
		params.winner = true
		
		assert submission.winner == false
		controller.updateWinnerStatus()

		assert response.redirectedUrl == "/admin/showSubmission/$submission.id"
		assert submission.winner == true
	}
	
	void testValidSubmissionInvalidVersionUpdateWinnerStatus() {
		populateValidParams(params)
		Submission submission = new Submission(params).save(flush:true)
		params.id = submission.id
		params.winner = true
		params.version = -1
		assert submission.winner == false
		controller.updateWinnerStatus()

		assert response.redirectedUrl == "/admin/showSubmission/$submission.id"
		assert submission.winner == false
	}
	
	void testNoSubmissionUpdateGrandTotal() {
		controller.updateGrandTotal()
		
		assert flash.message != null
		assert response.redirectedUrl == "/admin/index"
	}
	
	void test1Submission0RankUpdateGrandTotal() {
		populateValidParams(params)
		Submission submission = new Submission(params).save(flush:true)
		controller.updateGrandTotal()
		
		assert flash.message != null
		assert response.redirectedUrl == "/admin/index"
		assert Submission.count() == 1
	}
	
	void test1Submission1RankUpdateGrandTotal() {
		populateValidParams(params)
		Submission submission = new Submission(params).save(flush:true,failOnError:true)
		params.submissions = submission
		Judge judge = new Judge(username:'peterj',password:'a',firstName:'PeterJ',lastName:'Griffon',enabled:true)
		judge.metaClass.encodePassword = {'b'}
		judge.save(flush:true,failOnError:true)
		params.judges = judge
		new Rank(params).save(flush:true,failOnError:true)
		controller.updateGrandTotal()
		
		assert flash.message != null
		assert response.redirectedUrl == "/admin/index"
		assert Submission.count() == 1
		assert Rank.count() == 1
		assert submission.grandTotal == 24
	}

	void testMutliSubmissionMultiRankUpdateGrandTotal() {
		populateValidParams(params)
		Submission submission = new Submission(params).save(flush:true,failOnError:true)
		Submission submission2 = new Submission(params).save(flush:true,failOnError:true)
		params.submissions = submission
		Judge judge = new Judge(username:'peterj',password:'a',firstName:'PeterJ',lastName:'Griffon',enabled:true)
		judge.metaClass.encodePassword = {'b'}
		judge.save(flush:true)
		params.judges = judge
		new Rank(params).save(flush:true,failOnError:true)
		assert submission.grandTotal == 0
		assert submission2.grandTotal == 0
		
		controller.updateGrandTotal()
		
		assert response.redirectedUrl == "/admin/index"
		assert Submission.count() == 2
		assert Rank.count() == 1
		assert submission.grandTotal == 24
		assert submission2.grandTotal == 0
	}
	
	void testInvalidList() {
		controller.list()
		
		assert SecUser.count() == 0
		assert response.redirectedUrl == "/admin/create"
	}
	
	void test1AdminNoRoleList() {
		populateValidParams(params)
		SecUser user = new SecUser(username:'peterj',password:'a',firstName:'PeterJ',lastName:'Griffon',enabled:true)
		user.metaClass.encodePassword = {'b'}
		user.save(flush:true,failOnError:true)
		def model = controller.list()
		
		assert flash.message != null
		assert SecUser.count() == 1
		assert response.redirectedUrl == "/admin/create"
	}
	
	void testNoAdmin1RoleList() {
		populateValidParams(params)
		SecRole role = new SecRole(params).save(flush:true)
		def model = controller.list()
		
		assert flash.message != null
		assert SecUser.count() == 0
		assert SecRole.count() == 1
		assert response.redirectedUrl == "/admin/create"
	}
	
	void test1AdminList() {
		populateValidParams(params)
		SecRole role = new SecRole(params).save(flush:true)
		SecUser user = new SecUser(username:'peterj',password:'a',firstName:'PeterJ',lastName:'Griffon',enabled:true)
		user.metaClass.encodePassword = {'b'}
		user.save(flush:true)
		SecUserSecRole.create user,role
		def model = controller.list()
		
		assert flash.message == null
		assert SecUser.count() == 1
		assert model.secUserInstanceTotal == 1
	}
	
	void testMoreThan1AdminList() {
		populateValidParams(params)
		SecRole role = new SecRole(params).save(flush:true)
		SecUser user = new SecUser(username:'peterj',password:'a',firstName:'PeterJ',lastName:'Griffon',enabled:true)
		user.metaClass.encodePassword = {'b'}
		user.save(flush:true)
		SecUserSecRole.create user,role
		SecUser user2 = new SecUser(username:'peterj2',password:'a',firstName:'PeterJ',lastName:'Griffon',enabled:true)
		user2.metaClass.encodePassword = {'b'}
		user2.save(flush:true)
		SecUserSecRole.create user2,role
		def model = controller.list()
		
		assert flash.message == null
		assert SecUser.count() == 2
		assert model.secUserInstanceTotal == 2
	}
	
	void testEmptyCreate() {
		def model = controller.create()
		
		assert flash.message == null
		assert model.secUserInstance != null
	}
    
	void testPrefilledParamsCreate() {
		params.username='peterj'
		params.password='a'
		params.firstName='PeterJ'
		params.lastName='Griffon'
		params.enabled=true
        def model = controller.create()

        assert flash.message == null
		assert model.secUserInstance != null
		assert model.secUserInstance.username == 'peterj'
    }
	
	void testInvalidSave() {
		controller.save()
		
		assert model.secUserInstance != null
		assert view == "/admin/create"
	}

	void testInvalidParamsInvalidSave() {
		params.username=''
		params.password='a'
		params.firstName='PeterJ'
		params.lastName='Griffon'
		params.enabled=true		
		controller.save()

		assert model.secUserInstance != null
		assert view == "/admin/create"
	}
	
	void testValidParamsValidSave() {
		params.username='peterj'
		params.password='aasd'
		params.firstName='PeterJ'
		params.lastName='Griffon'
		params.enabled=true
		controller.save()
		
		assert flash.message != null
		assert response.redirectedUrl == "/admin/show/1"
		assert SecUser.count() == 1
	}
	
	void testInvalidShow() {
		controller.show()
		
		assert flash.message != null
		assert response.redirectedUrl == "/admin/list"
	}
	
	void testValidShow() {
		SecUser user = new SecUser(username:'peterj',password:'a',firstName:'PeterJ',lastName:'Griffon',enabled:true).save(flush:true)
		params.id = user.id
		def model = controller.show()
		
		assert model.secUserInstance.username == 'peterj'
		assert SecUser.count() == 1
	}

	void testInvalidEdit() {
		controller.edit()
		
		assert flash.message != null
		assert response.redirectedUrl == "/admin/list"
	}

	void testValidEdit() {
		SecUser user = new SecUser(username:'peterj',password:'a',firstName:'PeterJ',lastName:'Griffon',enabled:true).save(flush:true)
		params.id = user.id
		def model = controller.edit()
		
		assert model.secUserInstance != null
		assert model.secUserInstance.username == 'peterj'
		assert model.secUserInstance == user
	}

	void testInvalidUpdate() {
		controller.update()
		
		assert flash.message != null
		assert response.redirectUrl == "/admin/list"
	}

	void testValidIDInvalidUpdate() {
		params.id == 2
		controller.update()
		
		assert flash.message != null
		assert response.redirectedUrl == "/admin/list"
	}

	void testInvalidParamsInvalidUpdate() {
		SecUser user = new SecUser(username:'peterj',password:'a',firstName:'PeterJ',lastName:'Griffon',enabled:true).save(flush:true)
		params.id = user.id
		params.username =''
		controller.update()
		
		assert flash.message != null
		assert response.redirectedUrl == "/admin/edit/$params.id"
	}

	void testOudatedVersionUpdate() {
		SecUser user = new SecUser(username:'peterj',password:'a',firstName:'PeterJ',lastName:'Griffon',enabled:true).save(flush:true)
		params.id = user.id
		params.version = -1
		controller.update()

		assert flash.message != null
		assert response.redirectedUrl == "/admin/edit/$params.id"
	}

	void testGoodParamsValidUpdate() {
		populateValidParams(params)
		SecUser user = new SecUser(params)
		user.metaClass.encodePassword= {'b'}
		user.metaClass.isDirty= {false}
		user.save(flush:true,failOnError:true)
		params.firstName = 'Panda'
		params.id = user.id
		controller.update()
		
		assert flash.message != null
		assert response.redirectedUrl == "/admin/show/$params.id"
		assert user.firstName == 'Panda'
	}

	void testUnchangedParamsValidUpdate() {
		populateValidParams(params)
		SecUser user = new SecUser(params).save(flush:true)
		params.id = user.id
		controller.update()

		assert response.redirectedUrl == "/admin/show/$params.id"
		assert flash.message != null
		assert user.username == 'peterj'
	}
	
	void testNoAdminsInvalidDelete() {
		controller.delete()
		
		assert flash.message != null
		assert response.redirectedUrl == "/admin/list"
	}
	
	void testValidAdminValidDelete() {
		populateValidParams(params)
		SecRole role = new SecRole(params).save(flush:true,failOnError:true)
		SecUser user = new SecUser(params)
		user.metaClass.encodePassword= {'b'}
		user.metaClass.isDirty= {false}
		user.save(flush:true,failOnError:true)
		SecUserSecRole.create user,role
		assert SecUser.count() == 1
		params.id = user.id
		controller.delete()
		
        assert SecUser.count() == 0
		assert response.redirectedUrl == "/admin/list"
	}
}