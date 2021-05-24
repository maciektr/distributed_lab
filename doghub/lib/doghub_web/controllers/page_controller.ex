defmodule DoghubWeb.PageController do
  use DoghubWeb, :controller

  alias DoghubWeb.ApiClient
  alias DoghubWeb.ProfileController

  @dog_breeds_url "https://dog.ceo/api/breeds/list/all"

  def index(conn, _params) do
    with {:ok, breeds} <- fetch_dog_breeds() do
      render(conn, "index.html", data: %{"breeds" => breeds})
    end
  end

  def result(conn, %{"name" => username, "breed" => breed} = _params) do
    with {:ok, profile} <- ProfileController.construct_profile(username, breed) do
      render(conn, "result.html", data: %{"profile" => profile})
    end
  end

  defp fetch_dog_breeds() do
    with {:ok, _, body} <- ApiClient.get(@dog_breeds_url) do
      with %{status: "success", message: breeds} <- body do
        result = breeds |> Map.keys() |> Enum.map(&Atom.to_string/1)
        {:ok, result}
      end
    end
  end
end
