import React from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";

const SearchComponent = ({onChange, onClick}) => {
    return (
    <div className="search-field">
        <input className="search-input" type="text" onChange={onChange}></input>
        <button className="search-button" onClick={onClick}><FontAwesomeIcon className="search-icon" icon={faMagnifyingGlass} /></button>
    </div>
    );
}

export default SearchComponent;