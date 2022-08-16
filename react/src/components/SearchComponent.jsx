import React from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";

const SearchComponent = ({ onChange, onClick }) => {
    return (
        <form>
            <div className="search-field">
                <input className="search-input" type="search" onChange={onChange} required="required"></input>
                <button className="search-button" type="submit" onClick={onClick}><FontAwesomeIcon className="search-icon" icon={faMagnifyingGlass} /></button>
            </div>
        </form>
    );
}

export default SearchComponent;