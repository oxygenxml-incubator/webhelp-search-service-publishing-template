import React from 'react';

const SubmitButton = () => {
    return (<button className="aa-SubmitButton" type="submit" title="Submit">
        <svg width="20" height="20" viewBox="0 0 20 20">
            <path
                d="M14.386 14.386l4.0877 4.0877-4.0877-4.0877c-2.9418 2.9419-7.7115 2.9419-10.6533 0-2.9419-2.9418-2.9419-7.7115 0-10.6533 2.9418-2.9419 7.7115-2.9419 10.6533 0 2.9419 2.9418 2.9419 7.7115 0 10.6533z"
                fill="none"
                fillRule="evenodd"
                stroke="currentColor"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="1.4"
            />
        </svg>
    </button>);
}

export default SubmitButton;