class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		"/login/$action"(controller:"login")
		"/logout/$action"(controller:"logout")

		"/"(view:"/index")
		"500"(view:'/error')
		"/judging"(controller:'judge')
		"/pastSubmissions"(controller:'submissionArchive',action:'list')
		"/pastSubmissions/$action?/$id?"(controller:'submissionArchive')
		"/winners"(controller:"submission",action:"listWinners")
		"/pastWinners"(controller:"submissionArchive",action:"listWinners")
	}
}
