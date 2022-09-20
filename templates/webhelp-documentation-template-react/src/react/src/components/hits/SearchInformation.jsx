import React from 'react';

/**
 * Class that renders information about the search request.
 * @param {*} nHits is the number of hits by the given query. 
 * @param {*} query is the current query.
 * @param {*} page is the current selected page.
 * @param {*} pages is the number of total pages.
 * @returns a container with information about the search request.
 */
const SearchInformation = ({nHits, query, page, pages}) => {
    return (
        <div className="information-container">
            <span className="hits-information">{nHits + " document(s) found for "}<strong>{query + "."}</strong></span>
            <span className="page-information">{"Page " + page + "/" + pages}</span>
        </div>
    );
}

export default SearchInformation;