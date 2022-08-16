import React, {useState} from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";

const SearchComponent = ({performSearch}) => {
    // Create a state variable that stores the search term.
    const [searchTerm, setSearchTerm] = useState("");

    return (
        <form method="post">
            <div className="search-field">
                <input className="search-input" type="search" onChange={(e) => setSearchTerm(e.target.value.trim())} required="required"></input>
                <button className="search-button" type="submit" onClick={(e) => {e.preventDefault(), performSearch(searchTerm, 0);}}><FontAwesomeIcon className="search-icon" icon={faMagnifyingGlass} /></button>
            </div>
        </form>
    );
}

export default SearchComponent;