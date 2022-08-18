import React from 'react';
//import SearchComponent from './SearchComponent.jsx';
import AutocompleteComponent from './AutocompleteComponent.jsx';

const SearchComponentProxy = ({ performSearch }) => {
    //return (<SearchComponent performSearch={performSearch} />);
    return (<AutocompleteComponent />);
}

export default SearchComponentProxy