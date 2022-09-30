# WebHelp Search Service Publishing Template

[![Build project documentation and publish to Netlify](https://github.com/oxygenxml-incubator/webhelp-search-service-publishing-template/actions/workflows/dita-build-deploy-netlify.yml/badge.svg)](https://github.com/oxygenxml-incubator/webhelp-search-service-publishing-template/actions/workflows/dita-build-deploy-netlify.yml) [![Java CI with Maven](https://github.com/oxygenxml-incubator/webhelp-search-service-publishing-template/actions/workflows/maven-build-action.yml/badge.svg)](https://github.com/oxygenxml-incubator/webhelp-search-service-publishing-template/actions/workflows/maven-build-action.yml) [![Sonar Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=webhelp-search-service-publishing-template&metric=alert_status&branch=development)](https://sonarcloud.io/summary/new_code?id=webhelp-search-service-publishing-template)

We'll create a publishing template to demonstrates how to integrate a search service with WebHelp transformation. 

The project documentation is available *[here.](https://webhelp-search-service-template.netlify.app/)*<br>

How to replace internal WebHelp search engine with Algolia?
	- Create an Algolia index by crawling a WebHelp documentation
	- Item
	- Setup searchable attributes and ranking criteria
	- Highlight matching words enhancement
	- Change search engine in Webhelp template
	- Replace short description with a snippet of matching words enhancement
	- Integrate Algolia Autocomplete widget in WebHelp transformation

How to replace search result presentation to add filter capabilities?
	- Update Algolia Crawler to index profiling conditions
	- Setup a React application to implement results presentation
	- Add filter component in search page
	- Publishing template to search over multiple documentation
	- Add support for labels/tags in WebHelp pages
	- Add breadcrumb to results
	
[Continous publishing with GitHub Actions and Netlify](https://webhelp-search-service-template.netlify.app/topics/cd_with_github_actions.html)

Example with profiling values from DITA is available *[here.](https://syncro-phone.netlify.app/)*<br>
Example with multiple documentations is available *[here.](https://multiple-docs.netlify.app/)*

Copyright and License
---------------------
Copyright 2022 Syncro Soft, SRL.

This project is licensed under [Apache License 2.0](https://github.com/oxygenxml-incubator/repo-template/blob/master/LICENSE)

