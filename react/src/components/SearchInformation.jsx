import React from 'react';

const SearchInformation = ({nHits, query, page, pages}) => {
    return (
        <div className="information-container">
            <span className="hits-information">{nHits + " document(s) found for "}<strong>{query + "."}</strong></span>
            <span className="page-information">{"Page " + page + "/" + pages}</span>
        </div>
    );
}

export default SearchInformation;