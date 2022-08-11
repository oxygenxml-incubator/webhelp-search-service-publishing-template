import React from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";

const SearchComponent = () => {
    return (
    <div className="search-field">
        <input className="search-input" type="text"></input>
        <button className="search-button"><FontAwesomeIcon className="search-icon" icon={faMagnifyingGlass} /></button>
    </div>
    );
}

export default SearchComponent;