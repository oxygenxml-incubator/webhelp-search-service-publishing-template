import { createAutocomplete } from '@algolia/autocomplete-core';
import { getAlgoliaResults } from '@algolia/autocomplete-preset-algolia';

import React, { useState, useMemo, useEffect, useRef } from 'react';

import AutocompleteList from './AutocompleteList.jsx';

import ClearButton from '.././buttons/ClearButton.jsx';
import SubmitButton from ".././buttons/SubmitButton.jsx"

import { searchableAttributes, facetFilters } from '../filter/FilterContainer.jsx';

import '@algolia/autocomplete-theme-classic';

const AutocompleteComponent = (props) => {
    const [errorMessage, setErrorMessage] = useState("");

    const [autocompleteState, setAutocompleteState] = useState({
        collections: [],
        completion: null,
        context: {},
        isOpen: false,
        query: '',
        activeItemId: null,
        status: 'idle',
    });

    const autocomplete = useMemo(() => createAutocomplete({
        onSubmit({ state }) {
            if (state.query.trim().length !== 0)
                props.performSearch(state.query, 0, [...searchableAttributes], [...facetFilters])
            else
                setErrorMessage("Please fill in the empty fields!")
        },

        onStateChange({ state }) {
            if (state.query.trim().localeCompare(""))
                setErrorMessage("")

            setAutocompleteState(state);
        },

        navigator: {
            navigate({ item }) {
                setAutocompleteState({...autocompleteState, query: item.title})
                props.performSearch(item.title, 0, [...searchableAttributes], [...facetFilters])
            },
        },

        getSources() {
            return [
                {
                    sourceId: 'documents',

                    getItemUrl({ item }) {
                        return item.objectID;
                    },

                    getItems({ query }) {
                        return getAlgoliaResults({
                            searchClient: props.searchClient,
                            queries: [
                                {
                                    indexName: "mobile-phone",
                                    query,
                                    params: {
                                        hitsPerPage: 5,
                                        highlightPreTag: '<mark>',
                                        highlightPostTag: '</mark>'
                                    },
                                },
                            ],
                        });
                    },
                },
            ];
        }
    }, props), [props]);

    const inputRef = useRef(null);
    const formRef = useRef(null);
    const panelRef = useRef(null);
    const { getEnvironmentProps } = autocomplete;

    useEffect(() => {
        if (!formRef.current || !panelRef.current || !inputRef.current) {
            return undefined;
        }
        const { onTouchStart, onTouchMove } = getEnvironmentProps({
            formElement: formRef.current,
            inputElement: inputRef.current,
            panelElement: panelRef.current,
        });

        window.addEventListener('touchstart', onTouchStart);
        window.addEventListener('touchmove', onTouchMove);

        return () => {
            window.removeEventListener('touchstart', onTouchStart);
            window.removeEventListener('touchmove', onTouchMove);
        };

    }, [getEnvironmentProps, formRef, inputRef, panelRef]);

    return (
        <div className="aa-Autocomplete" {...autocomplete.getRootProps({})}>
            <form
                ref={formRef}
                className="aa-Form"
                {...autocomplete.getFormProps({ inputElement: inputRef.current })}
            >
                <div className="aa-InputWrapperPrefix">
                    <label className="aa-Label" {...autocomplete.getLabelProps({})}>
                        <SubmitButton />
                    </label>
                </div>
                <div className="aa-InputWrapper">
                    <input
                        className="aa-Input"
                        ref={inputRef}
                        {...autocomplete.getInputProps({ inputElement: inputRef.current })}
                        placeholder={autocompleteState.query}
                    />
                </div>
                <div className="aa-InputWrapperSuffix">
                    <ClearButton />
                </div>
            </form>

            {autocompleteState.isOpen && (
                <div
                    ref={panelRef}
                    className={[
                        'aa-Panel',
                        'aa-Panel--desktop',
                        autocompleteState.status === 'stalled' && 'aa-Panel--stalled',
                    ]
                        .filter(Boolean)
                        .join(' ')}
                    {...autocomplete.getPanelProps({})}
                >
                    <div className="aa-PanelLayout aa-Panel--scrollable">
                        {autocompleteState.collections.map((collection, index) => {
                            return (
                                <AutocompleteList key={`source-${index}`} performSearch={props.performSearch} index={index} items={collection.items} source={collection.source} autocomplete={autocomplete} />
                            );
                        })}
                    </div>
                </div>
            )}
            <span className="error-message">{errorMessage}</span>
        </div>
    );
}

export default AutocompleteComponent;
