import React, { useState } from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";

const SearchComponent = ({ performSearch }) => {
    // Create a state variable that stores the search term.
    const [searchTerm, setSearchTerm] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const performSearchInternal = (e) => {
        e.preventDefault();

        if (searchTerm.length === 0)
            setErrorMessage("Please fill in the empty field!");
        else
            performSearch(searchTerm, 0);
    }

    const onChangeInput = (e) => {
        setErrorMessage("");

        setSearchTerm(e.target.value.trim());
    }

    return (
        <>
            <form>
                <div className="search-field">
                    <input className="search-input" type="search" onChange={(e) => { onChangeInput(e) }} required="required"></input>
                    <button className="search-button" type="submit" onClick={(e) => { performSearchInternal(e) }}><FontAwesomeIcon className="search-icon" icon={faMagnifyingGlass} /></button>
                </div>
            </form>
            <span className="error-message">{errorMessage}</span>
        </>
    );
}

export default SearchComponent;