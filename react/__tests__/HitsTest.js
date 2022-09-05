import { render, screen } from "@testing-library/react";
import HitsItem from "../src/components/hits/HitsItem.jsx";
import HitsList from "../src/components/hits/HitsList.jsx";
import SearchInformation from "../src/components/hits/SearchInformation.jsx";
import "@testing-library/jest-dom";
import React from "react";

test("checks the hit item", () => {
  render(<HitsItem title="Title" description="Description" url="#" />);

  expect(screen.getByText("Title")).toBeInTheDocument();
  expect(screen.getByText("Description")).toBeInTheDocument();
  expect(screen.getByText("Title")).toHaveAttribute("href", "#");
});

test("checks the hits list with hits", () => {
  render(
    <HitsList
      hits={[
        { objectID: "#", title: "Title", shortDescription: "Description" },
      ]}
    />
  );

  expect(screen.getByText("Title")).toBeInTheDocument();
  expect(screen.getByText("Description")).toBeInTheDocument();
  expect(screen.getByText("Title")).toHaveAttribute("href", "#");
});

test("checks the hits list with no hits", () => {
  render(<HitsList />);

  expect(screen.getByText("No results found!")).toBeInTheDocument();
});

test("checks the search information", () => {
  const { container } = render(
    <SearchInformation nHits={2} query="Something" page={1} pages={2} />
  );

  expect(
    container.getElementsByClassName("hits-information")[0]
  ).toHaveTextContent("2 document(s) found for Something.");
  expect(
    container.getElementsByClassName("page-information")[0]
  ).toHaveTextContent("Page 1/2");
});
