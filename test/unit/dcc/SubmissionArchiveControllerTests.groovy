package dcc

import org.junit.*
import grails.test.mixin.*

@TestFor(SubmissionArchiveController)
@Mock([SubmissionArchive,Submission,Rank])
class SubmissionArchiveControllerTests {

    def populateValidParams(params) {
        assert params != null
		params["category"] = "Cool"
		params["title"] = "Great Project"
		params["author"] = "Todd Dubb"
		params["emailAddr"] = "mdub@email.com"
		params["link"] = "https://coollinks.com"
		params["contestYear"] = 2013
		params["grandTotal"] = 200
    }

	@Before
	void setUp() {
			controller.submissionArchiveService = new SubmissionArchiveService()
	}
	
	void testIndex() {
        controller.index()
        assert view == "/submissionArchive/index"
    }

    void testEmptyList() {
        def model = controller.list()

        assert model == null
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message != null
    }

	void test1RecordList() {
		populateValidParams(params)
		SubmissionArchive sub = new SubmissionArchive(params).save(flush:true)
		assert sub.id != null
		
		def model = controller.list()

		assert model.submissionArchiveInstanceList.size() == 1
		assert model.submissionArchiveInstanceTotal == 1
		assert model.submissionArchiveInstanceList[0].title == "Great Project"
		assert flash.message == null
	}

	void testMoreThan1RecordList() {
		populateValidParams(params)
		new SubmissionArchive(params).save(flush:true)
		new SubmissionArchive(params).save(flush:true)
		def model = controller.list()

		assert model.submissionArchiveInstanceList.size() >= 2
		assert model.submissionArchiveInstanceTotal >= 2
		assert flash.message == null
	}
	
	void testEmptyWinnerList() {
		def model = controller.listWinners()

		assert model == null
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message != null
	}

	void test1RecordNoWinnerList() {
		populateValidParams(params)
		SubmissionArchive sub = new SubmissionArchive(params).save(flush:true)
		def model = controller.listWinners()

		assert model == null
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message != null
	}
	
	void test1Record1WinnerList() {
		populateValidParams(params)
		params.winner = true
		SubmissionArchive sub = new SubmissionArchive(params).save(flush:true)
		def model = controller.listWinners()

		assert model.submissionArchiveInstanceList.size() == 1
		assert model.submissionArchiveInstanceTotal == 1
		assert model.submissionArchiveInstanceList[0].title == "Great Project"
		assert model.submissionArchiveInstanceList[0].winner == true
	}
	
	void testMoreThan1Record1WinnerList() {
		populateValidParams(params)
		params.winner = false
		new SubmissionArchive(params).save(flush:true)
		new SubmissionArchive(params).save(flush:true)
		params.winner = true
		new SubmissionArchive(params).save(flush:true)
		def model = controller.listWinners()

		assert model.submissionArchiveInstanceList.size() == 1
		assert model.submissionArchiveInstanceTotal == 1
		assert model.submissionArchiveInstanceList[0].title == "Great Project"
		assert model.submissionArchiveInstanceList[0].winner == true
	}

    void testEmptyCreate() {
        controller.create()

        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
    }

    void testPrefilledParamsCreate() {
        populateValidParams(params)
        controller.create()

        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
    }

    void testNoValuesInvalidSave() {
        controller.save()

        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
    }
	
	void testInvalidValuesInvalidSave() {
        populateValidParams(params)
		params.link = ""
		params.email = "invalid"
		controller.save()

        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
	}

    void testValidSave() {
        populateValidParams(params)
        controller.save()

        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
    }

    void testInvalidShow() {
        def model = controller.show()

        assert model == null
		assert flash.message != null
        assert response.redirectedUrl == '/submissionArchive/list'
    }

	void testValidShow() {
        populateValidParams(params)
        SubmissionArchive submissionArchive = new SubmissionArchive(params)
        assert submissionArchive.save(flush:true) != null
        params.id = submissionArchive.id
        def model = controller.show()

        assert model.submissionArchiveInstance == submissionArchive
		assert model.submissionArchiveInstance.category == "Cool"
        assert flash.message == null
    }

	void testMultiSubmissionsShow() {
        populateValidParams(params)
        SubmissionArchive submissionArchive = new SubmissionArchive(params)
		SubmissionArchive sub = new SubmissionArchive(category:"Wicked",title:"Awesome Project",author:"Todd",emailAddr:"Toddw@domain.com",link:"google.com",winner:false,contestYear:2012)
        assert submissionArchive.save(flush:true) != null
		assert sub.save(flush:true) != null
        params.id = submissionArchive.id
		params.id = sub.id
        def model = controller.show()

        assert model.submissionArchiveInstance != submissionArchive
        assert model.submissionArchiveInstance == sub
		assert model.submissionArchiveInstance.category != "Cool"
		assert model.submissionArchiveInstance.contestYear == 2012
        assert flash.message == null		
    }

    void testInvalidShowWinner() {
        controller.showWinner()

		assert model == [:]
        assert flash.message != null
        assert response.redirectedUrl == '/submissionArchive/listWinners'
    }

    void testWinnerFalseValidSubmissionShowWinner() {
        populateValidParams(params)
		params.winner = false
        SubmissionArchive submissionArchive = new SubmissionArchive(params)
        assert submissionArchive.save(flush:true) != null
        controller.showWinner()

        assert model == [:]
		assert flash.message != null
        assert response.redirectedUrl == '/submissionArchive/listWinners'
    }
	
	void testValidShowWinner() {
        populateValidParams(params)
		params.winner = true
        SubmissionArchive submissionArchive = new SubmissionArchive(params)
        assert submissionArchive.save(flush:true) != null
        params.id = submissionArchive.id
        def model = controller.showWinner()

        assert model.submissionArchiveInstance == submissionArchive
		assert model.submissionArchiveInstance.category == "Cool"
		assert model.submissionArchiveInstance.winner == true
		assert model.submissionArchiveInstance.contestYear == 2013
        assert flash.message == null
    }

	void testMultiSubmissionsShowWinner() {
        populateValidParams(params)
		params.winner = true
        SubmissionArchive submissionArchive = new SubmissionArchive(params)
		params.contestYear = 2012
		SubmissionArchive sub = new SubmissionArchive(params)
        assert submissionArchive.save(flush:true) != null
		assert sub.save(flush:true) != null
        params.id = submissionArchive.id
		params.id = sub.id
        def model = controller.showWinner()

        assert model.submissionArchiveInstance != submissionArchive
        assert model.submissionArchiveInstance == sub
		assert model.submissionArchiveInstance.winner == true
		assert model.submissionArchiveInstance.category == "Cool"
		assert model.submissionArchiveInstance.contestYear == 2012
        assert flash.message == null		
    }

    void testInvalidEdit() {
        controller.edit()

        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
	}

    void testValidEdit() {
        populateValidParams(params)
        SubmissionArchive submissionArchive = new SubmissionArchive(params)

        assert submissionArchive.save(flush:true) != null

        params.id = submissionArchive.id
        controller.edit()
		
        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
    }

    void testNullIDInvalidUpdate() {
        controller.update()

        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
    }

    void testGoodIDInvalidUpdate() {
		params.id = 2
        controller.update()

        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
    }
	
	void testInvalidParamsInvalidUpdate() {
		populateValidParams(params)
		SubmissionArchive submissionArchive = new SubmissionArchive(params)
		assert submissionArchive.save(flush:true) != null
		params.id = submissionArchive.id
		// test invalid parameters in update
		params.email = "invalid"
		params.link = ""

		controller.update()

        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
	}

    void testOutdatedVersionInvalidUpdate() {
        populateValidParams(params)
		SubmissionArchive submissionArchive = new SubmissionArchive(params)
		assert submissionArchive.save(flush:true) != null
        params.id = submissionArchive.id
        params.version = -1
        controller.update()
		
        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
    }

	void testGoodParamsValidUpdate() {
		populateValidParams(params)
		SubmissionArchive submissionArchive = new SubmissionArchive(params)

		assert submissionArchive.save(flush:true) != null
		params.email='bob@domain.com'
		controller.update()

        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
	}

	void testUnchangedParamsValidUpdate() {
		populateValidParams(params)
		SubmissionArchive submissionArchive = new SubmissionArchive(params)

		assert submissionArchive.save(flush:true) != null
		controller.update()

        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
	}

	void testEmptySubmissionInvalidDelete() {
		controller.delete()
		
        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
	}

    void testGoodSubmissionValidDelete() {
        populateValidParams(params)
        SubmissionArchive submissionArchive = new SubmissionArchive(params)

        assert submissionArchive.save() != null
        assert SubmissionArchive.count() == 1

        params.id = submissionArchive.id
        controller.delete()

        assert model == [:]
		assert response.redirectedUrl == "/submissionArchive/index"
		assert flash.message == "This method is inaccessible"
    }
	
	void testNoSubmissionsArchiveSubmissions() {
		controller.archiveSubmissions()
		
		assert flash.message != null
		assert response.redirectedUrl == "/admin/"
	}
	
	void test1SubmissionArchiveSubmissions() {
		new Submission(category:"Cool",title:"Title",author:"Author",emailAddr:"email@place.com",link:"http://google.com").save(flush:true,failOnError:true)
		controller.archiveSubmissions()
		def sub = Submission.get(1)
		
		assert flash.message == null
		assert Submission.count() == 1
		assert sub.category == "Cool"
	}
	
	void testMoreThan1SubmissionArchiveSubmissions() {
		new Submission(category:"Cool",title:"Title",author:"Author",emailAddr:"email@place.com",link:"http://google.com").save(flush:true,failOnError:true)
		new Submission(category:"Cool 2",title:"Title 2",author:"Author",emailAddr:"email2@place.com",link:"http://google.com").save(flush:true,failOnError:true)
		controller.archiveSubmissions()
		def subs = Submission.list()
		
		assert flash.message == null
		assert Submission.count() == 2
		assert subs[0].category == "Cool"
		assert subs[1].title == "Title 2"
	}
	
	void testInvalidMoveSubmissions() {
		controller.moveSubmissions()
		
		assert flash.message != null
		assert response.redirectedUrl == "/submissionArchive/archiveSubmissions"
	}
	
	void testValidSubmissionNoContestYearMoveSubmissions() {
		new Submission(category:"Cool",title:"Title",author:"Author",emailAddr:"email@place.com",link:"http://google.com").save(flush:true,failOnError:true)
		controller.moveSubmissions()
		
		assert flash.message != null
		assert response.redirectedUrl == "/submissionArchive/archiveSubmissions"
		assert Submission.count() == 1
		assert SubmissionArchive.count() == 0
	}

	void test1SubmissionMoveSubmissions() {
		new Submission(category:"Cool",title:"Title",author:"Author",emailAddr:"email@place.com",link:"http://google.com").save(flush:true,failOnError:true)
		params.contestYear = "2012"
		controller.moveSubmissions()
		def subArch = SubmissionArchive.get(1)
		
		assert response.redirectedUrl == "/submissionArchive/index"
		assert Submission.count() == 0
		assert SubmissionArchive.count() == 1
		assert subArch.title == "Title"
	}
	
	void testMoreThan1SubmissionMoveSubmissions() {
		new Submission(category:"Cool",title:"Title",author:"Author",emailAddr:"email@place.com",link:"http://google.com").save(flush:true,failOnError:true)
		new Submission(category:"Cool",title:"Title 2",author:"Author 2",emailAddr:"email@place.com",link:"http://google.com").save(flush:true,failOnError:true)
		params.contestYear = "2012"
		controller.moveSubmissions()
		def subArch = SubmissionArchive.get(1)
		
		assert response.redirectedUrl == "/submissionArchive/index"
		assert Submission.count() == 0
		assert SubmissionArchive.count() == 2
		assert subArch.title == "Title"
	}
}