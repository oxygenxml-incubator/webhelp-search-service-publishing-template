import React from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";

const SearchButton = () => {
    return (<button><FontAwesomeIcon icon={faMagnifyingGlass} /></button>);
}

export default SearchButton;