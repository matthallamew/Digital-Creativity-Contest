package dcc
import org.junit.*
import grails.test.mixin.*

@TestFor(SubmissionController)
@Mock(Submission)
class SubmissionControllerTests {

    def populateValidParams(params) {
        assert params != null
		params["category"] = "Cool"
		params["title"] = "Great Project"
		params["author"] = "Todd Dubb"
		params["emailAddr"] = "mdub@email.com"
		params["link"] = "https://coollinks.com"
    }

	@Before
	void setUp() {
			controller.submissionService = new SubmissionService()
	}
	
	void testIndex() {
        controller.index()

        assert "/submission/list" == response.redirectedUrl
    }

    void testEmptyList() {
        def model = controller.list()

        assert model.submissionInstanceList.size() == 0
        assert model.submissionInstanceTotal == 0
    }

	void test1ElementList() {
		Submission sub = new Submission(category:"Cool",title:"Awesome Project",author:"Todd",emailAddr:"toddw@domain.com",link:"google.com").save(flush:true)
		def model = controller.list()

		assert model.submissionInstanceList.size() == 1
		assert model.submissionInstanceTotal == 1
		assert model.submissionInstanceList[0].title == "Awesome Project"
	}

	void testMoreThan1ElementList() {
		new Submission(category:"Cool",title:"Awesome Project",author:"Todd",emailAddr:"toddw@domain.com",link:"google.com").save(flush:true)
		new Submission(category:"Cool",title:"Awesome Project 2",author:"Todd",emailAddr:"toddw@domain.com",link:"google.com").save(flush:true)
		def model = controller.list()

		assert model.submissionInstanceList.size() >= 2
		assert model.submissionInstanceTotal >= 2
	}
	
	void testEmptyWinnerList() {
		def model = controller.listWinners()

		assert model.submissionInstanceList.size() == 0
		assert model.submissionInstanceTotal == 0
	}

	void test1WinnerList() {
		Submission sub = new Submission(category:"Cool",title:"Awesome Project",author:"Todd",emailAddr:"toddw@domain.com",link:"google.com",winner:true).save(flush:true)
		def model = controller.listWinners()

		assert model.submissionInstanceList.size() == 1
		assert model.submissionInstanceTotal == 1
		assert model.submissionInstanceList[0].title == "Awesome Project"
	}

	void test1ElementNoWinnerList() {
		Submission sub = new Submission(category:"Cool",title:"Awesome Project",author:"Todd",emailAddr:"toddw@domain.com",link:"google.com",winner:false).save(flush:true)
		def model = controller.listWinners()

		assert model.submissionInstanceList.size() == 0
		assert model.submissionInstanceTotal == 0
	}
	
    void testEmptyCreate() {
        def model = controller.create()

        assert model.submissionInstance != null
    }

    void testPrefilledParamsCreate() {
        populateValidParams(params)
        def model = controller.create()

        assert model.submissionInstance != null
		assert controller.params.category == "Cool"
    }

    void testNoValuesInvalidSave() {
        controller.save()

        assert model.submissionInstance != null
        assert view == '/submission/create'
    }
	
	void testInvalidValuesInvalidSave() {
        populateValidParams(params)
		params.link = ""
		params.email = "invalid"
		controller.save()

		assert model.submissionInstance != null
		assert view == '/submission/create'
	}

    void testValidSave() {
        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/submission/confirmSubmission/1'
        assert controller.flash.message != null
        assert Submission.count() == 1
    }
    void testInvalidConfirmSubmission() {
        controller.confirmSubmission()

        assert flash.message != null
        assert response.redirectedUrl == '/submission/create'
    }

	void testValidConfirmSubmission() {
        populateValidParams(params)
        Submission submission = new Submission(params)
        assert submission.save(flush:true) != null
        params.id = submission.id
        def model = controller.confirmSubmission()
		
        assert model.submissionInstance == submission
		assert model.submissionInstance.category == "Cool"
        assert flash.message == null
    }

    void testInvalidShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/submission/list'
    }

	void testValidShow() {
        populateValidParams(params)
        Submission submission = new Submission(params)
        assert submission.save(flush:true) != null
        params.id = submission.id
        def model = controller.show()

        assert model.submissionInstance == submission
		assert model.submissionInstance.category == "Cool"
        assert flash.message == null
    }

	void testMultiSubmissionsShow() {
        populateValidParams(params)
        Submission submission = new Submission(params)
		Submission sub = new Submission(category:"Wicked",title:"Awesome Project",author:"Todd",emailAddr:"toddw@domain.com",link:"google.com",winner:false)
        assert submission.save(flush:true) != null
		assert sub.save(flush:true) != null
        params.id = submission.id
		params.id = sub.id
        def model = controller.show()

        assert model.submissionInstance != submission
        assert model.submissionInstance == sub
		assert model.submissionInstance.category != "Cool"
        assert flash.message == null		
    }

    void testInvalidShowWinner() {
        controller.showWinner()

        assert flash.message != null
        assert response.redirectedUrl == '/submission/listWinners'
    }

    void testWinnerFalseValidSubmissionShowWinner() {
        populateValidParams(params)
		params.winner = false
        Submission submission = new Submission(params)
        assert submission.save(flush:true) != null
        controller.showWinner()

        assert flash.message != null
        assert response.redirectedUrl == '/submission/listWinners'
    }
	
	void testValidShowWinner() {
        populateValidParams(params)
		params.winner = true
        Submission submission = new Submission(params)
        assert submission.save(flush:true) != null
        params.id = submission.id
        def model = controller.showWinner()

        assert model.submissionInstance == submission
		assert model.submissionInstance.category == "Cool"
		assert model.submissionInstance.winner == true
        assert flash.message == null
    }

	void testMultiSubmissionsShowWinner() {
        populateValidParams(params)
        Submission submission = new Submission(params)
		params.winner = true
		Submission sub = new Submission(category:"Wicked",title:"Awesome Project",author:"Todd",emailAddr:"toddw@domain.com",link:"google.com",winner:true)
        assert submission.save(flush:true) != null
		assert sub.save(flush:true) != null
        params.id = submission.id
		params.id = sub.id
        def model = controller.showWinner()

        assert model.submissionInstance != submission
        assert model.submissionInstance == sub
		assert model.submissionInstance.winner == true
		assert model.submissionInstance.category != "Cool"
        assert flash.message == null		
    }

    void testInvalidEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/submission/list'
	}

    void testValidEdit() {
        populateValidParams(params)
        Submission submission = new Submission(params)

        assert submission.save(flush:true) != null

        params.id = submission.id
        def model = controller.edit()
		
        assert model.submissionInstance == submission
		assert model.submissionInstance.category == "Cool"
        assert flash.message == null
    }

    void testNullIDInvalidUpdate() {
        def model = controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/submission/list'
		assert model == null
    }

    void testGoodIDInvalidUpdate() {
		params.id = 2
        def model = controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/submission/list'
		assert model == null
    }
	
	void testInvalidParamsInvalidUpdate() {
		populateValidParams(params)
		Submission submission = new Submission(params)
		assert submission.save(flush:true) != null
		params.id = submission.id
		// test invalid parameters in update
		params.email = "invalid"
		params.link = ""

		controller.update()

		assert response.redirectedUrl == "/submission/edit/${params.id}"
		assert flash.message != null
	}

    void testOutdatedVersionInvalidUpdate() {
        populateValidParams(params)
		Submission submission = new Submission(params)
		assert submission.save(flush:true) != null
        params.id = submission.id
        params.version = -1
        controller.update()
		
        assert response.redirectedUrl == "/submission/edit/${params.id}"
        assert flash.message != null
    }

	void testGoodParamsValidUpdate() {
		populateValidParams(params)
		Submission submission = new Submission(params)

		assert submission.save(flush:true) != null
		params.email='bob@domain.com'
		params.id = submission.id
		controller.update()

		assert response.redirectedUrl == "/submission/show/$params.id"
		assert flash.message != null
	}

	void testUnchangedParamsValidUpdate() {
		populateValidParams(params)
		Submission submission = new Submission(params)

		assert submission.save(flush:true) != null
		params.id = submission.id
		controller.update()

		assert response.redirectedUrl == "/submission/show/$params.id"
		assert flash.message != null
	}

	void testEmptySubmissionInvalidDelete() {
		controller.delete()
		
		assert flash.message != null
		assert response.redirectedUrl == '/submission/list'
	}

    void testGoodSubmissionValidDelete() {
        populateValidParams(params)
        Submission submission = new Submission(params)

        assert submission.save() != null
        assert Submission.count() == 1

        params.id = submission.id
        controller.delete()

        assert Submission.count() == 0
        assert Submission.get(submission.id) == null
        assert response.redirectedUrl == '/submission/list'
    }
}