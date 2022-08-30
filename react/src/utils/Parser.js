const Parser = {
  createFilters: (query) => {
    let filters = [];
    query = query.replace(/\s+/g, " ").split(" ");

    if (
      query.includes("and") ||
      query.includes("AND") ||
      query.includes("or") ||
      query.includes("OR")
    ) {
      for (let i = 0; i < query.length; i++) {
        if (query[i].toLowerCase() === "and") {
          console.log(query[i].toLowerCase());
          filters.push(query[i - 1]);
          filters.push(query[i + 1]);
        } else if (query[i].toLowerCase() === "or") {
          filters.push([query[i - 1], query[i + 1]]);
        }
      }
    } else {
      console.log(query);
      return query;
    }

    console.log(filters);
    return filters;
  },
};

export default Parser;
