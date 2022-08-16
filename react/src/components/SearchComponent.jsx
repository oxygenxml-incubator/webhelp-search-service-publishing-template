import React, { useState } from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";

const SearchComponent = ({ performSearch }) => {
    // Create a state variable that stores the search term.
    const [searchTerm, setSearchTerm] = useState("");
    const [wasPressed, setWasPressed] = useState(false);

    return (
        <>
            <form>
                <div className="search-field">
                    <input className="search-input" type="search" onChange={(e) => { setSearchTerm(e.target.value.trim()), setWasPressed(false) }} required="required"></input>
                    <button className="search-button" type="submit" onClick={(e) => { e.preventDefault(), searchTerm.length === 0 ? setWasPressed(true) : performSearch(searchTerm, 0) }}><FontAwesomeIcon className="search-icon" icon={faMagnifyingGlass} /></button>
                </div>
            </form>
            <span className="error-message">{wasPressed && "Please fill in the input field!"}</span>
        </>
    );
}

export default SearchComponent;