import { render, screen } from "@testing-library/react";
import FilterComponent from "../src/components/filter/FilterComponent.jsx";
import FilterContainer from "../src/components/filter/FilterContainer.jsx";
import { searchableAttributes } from "../src/components/filter/FilterContainer.jsx";
import userEvent from "@testing-library/user-event";
import "@testing-library/jest-dom";
import React from "react";

test("checks the filter component", () => {
  render(
    <FilterComponent
      title="Options"
      options={[
        {
          algoliaId: "Option",
          isFilter: false,
          id: "Option",
          description: "Option",
        },
      ]}
      setData={() => {}}
      isSetData={() => {}}
      query={"Something"}
    />
  );

  expect(screen.getByText("Options")).toBeInTheDocument();
  expect(screen.getByText("Option")).toBeInTheDocument();
  expect(screen.getByRole("checkbox")).toBeInTheDocument();
});

test("checks the filter container", async () => {
  render(
    <FilterContainer
      performSearch={() => {}}
      query={"something"}
      sections={[
        {
          title: "Find query in",
          options: [
            {
              id: "attribute-title",
              description: "Title",
              isFilter: false,
              algoliaId: "title",
            },
            {
              id: "attribute-shortDescription",
              description: "Short Description",
              isFilter: false,
              algoliaId: "shortDescription",
            },
            {
              id: "attribute-contents",
              description: "Contents",
              isFilter: false,
              algoliaId: "contents",
            },
          ],
        },
      ]}
    />
  );

  await userEvent.click(screen.getByText("Contents"));

  expect(searchableAttributes.size).toBe(1);
  expect(screen.getByText("Find query in")).toBeInTheDocument();
  expect(screen.getByText("Title")).toBeInTheDocument();
  expect(screen.getByText("Short Description")).toBeInTheDocument();
  expect(screen.getByText("Contents")).toBeInTheDocument();
});
