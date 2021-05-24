defmodule DoghubWeb.PageController do
  use DoghubWeb, :controller

  alias DoghubWeb.ApiClient

  @dog_breeds_url "https://dog.ceo/api/breeds/list/all"

  def index(conn, _params) do
    with {:ok, breeds} <- fetch_dog_breeds() do
      data = %{"breeds" => breeds}
      render(conn, "index.html", data: data)
    end
  end

  def fetch_dog_breeds() do
    with {:ok, _, body} <- ApiClient.get(@dog_breeds_url) do
      with %{status: "success", message: breeds} <- body do
        result = breeds |> Map.keys() |> Enum.map(&Atom.to_string/1)
        {:ok, result}
      end
    end
  end
end
