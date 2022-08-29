const regexPattern = /[_a-zA-Z0-9]+:[a-zA-Z0-9]+/g;

const Parser = {
  findMatches: (query) => {
    return query.match(regexPattern);
  },
};

export default Parser;
