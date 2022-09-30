# WebHelp Search Service Publishing Template

[![Build project documentation and publish to Netlify](https://github.com/oxygenxml-incubator/webhelp-search-service-publishing-template/actions/workflows/dita-build-deploy-netlify.yml/badge.svg)](https://github.com/oxygenxml-incubator/webhelp-search-service-publishing-template/actions/workflows/dita-build-deploy-netlify.yml) [![Java CI with Maven](https://github.com/oxygenxml-incubator/webhelp-search-service-publishing-template/actions/workflows/maven-build-action.yml/badge.svg)](https://github.com/oxygenxml-incubator/webhelp-search-service-publishing-template/actions/workflows/maven-build-action.yml) [![Sonar Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=webhelp-search-service-publishing-template&metric=alert_status&branch=development)](https://sonarcloud.io/summary/new_code?id=webhelp-search-service-publishing-template)

We'll create a publishing template to demonstrates how to integrate a search service with WebHelp transformation. 

The project documentation is available *[here.](https://webhelp-search-service-template.netlify.app/)*<br>

## Project Outline: <br/>	

1. [How to replace internal WebHelp search engine with Algolia?](https://webhelp-search-service-template.netlify.app/topics/template_to_replace_search_engine.html)<br/>
	- Create an Algolia index by crawling a WebHelp documentation<br/>	
	- Setup searchable attributes and ranking criteria<br/>	
	- Highlight matching words enhancement<br/>	
	- Change search engine in Webhelp template<br/>	
	- Replace short description with a snippet of matching words enhancement<br/>	
	- Integrate Algolia Autocomplete widget in WebHelp transformation<br/>	

2. [How to replace search result presentation to add filter capabilities?](https://webhelp-search-service-template.netlify.app/topics/template_to_replace_results_page.html)<br/>	
	- Update Algolia Crawler to index profiling conditions<br/>	
	- Setup a React application to implement results presentation<br/>	
	- Add filter component in search page<br/>	
	- Publishing template to search over multiple documentation<br/>	
	- Add support for labels/tags in WebHelp pages<br/>	
	- Add breadcrumb to results<br/>	
	
3. [Continous publishing with GitHub Actions and Netlify](https://webhelp-search-service-template.netlify.app/topics/cd_with_github_actions.html)


Example with profiling values from DITA is available *[here.](https://syncro-phone.netlify.app/)*<br>
Example with multiple documentations is available *[here.](https://multiple-docs.netlify.app/)*

Copyright and License
---------------------
Copyright 2022 Syncro Soft, SRL.

This project is licensed under [Apache License 2.0](https://github.com/oxygenxml-incubator/repo-template/blob/master/LICENSE)

