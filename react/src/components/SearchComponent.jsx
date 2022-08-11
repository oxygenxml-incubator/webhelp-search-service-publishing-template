import React from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";

const SearchComponent = () => {
    return (
    <div className="search-field">
        <input type="text"></input>
        <button><FontAwesomeIcon icon={faMagnifyingGlass} /></button>
    </div>
    );
}

export default SearchComponent;