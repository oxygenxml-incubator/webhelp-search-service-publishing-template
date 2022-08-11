import React from 'react';

const SearchInformation = ({hitsInformation, pageInformation}) => {
    return (
        <div className="information-container">
            <span className="hits-information">{hitsInformation}</span>
            <span className="page-information">{pageInformation}</span>
        </div>
    );
}

export default SearchInformation;