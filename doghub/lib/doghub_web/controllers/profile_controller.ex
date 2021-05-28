defmodule DoghubWeb.ProfileController do
  use DoghubWeb, :controller

  alias DoghubWeb.ApiClient

  @dog_fact_url "https://dog-fact.herokuapp.com/api/v1/resources/dogs?number=1"
  @dog_photo_url "https://dog.ceo/api/breeds/image/random"
  @github_user_fetch_url "https://api.github.com/users/"

  def get(conn, %{"name" => username} = params) do
    breed = Map.get(params, "breed")

    with {:ok, body} <- construct_profile(username, breed) do
      json(conn, body)
    end
  end

  def construct_profile(username, breed) do
    with {:ok, dog_fact} <- fetch_dog_fact(),
         {:ok, dog_photo} <- fetch_dog_photo(breed, username),
         {:ok, github_profile} <- fetch_github_profile(username) do
      result =
        github_profile
        |> Map.put(:avatar_url, dog_photo)
        |> Map.put(:bio, dog_fact)

      repos_url = Map.get(github_profile, :repos_url)

      with {:ok, repos} <- fetch_repo_list(repos_url) do
        result = result |> Map.put(:repos, repos)
        {:ok, result}
      end
    end
  end

  defp fetch_repo_list(url) do
    with {:ok, _, body} <- ApiClient.get(url) do
      parse_node = fn node ->
        node |> Map.take([:full_name, :html_url])
      end

      {:ok, body |> Enum.map(parse_node)}
    end
  end

  defp fetch_github_profile(username) do
    with {:ok, _, body} <- ApiClient.get("#{@github_user_fetch_url}#{username}") do
      {:ok, body}
    end
  end

  defp fetch_dog_fact do
    with {:ok, _, body} <- ApiClient.get(@dog_fact_url) do
      with %{fact: fact} = List.first(body) do
        {:ok, fact}
      end
    end
  end

  defp fetch_dog_photo(breed \\ nil, name \\ nil) do
    with {:ok, url} <- fetch_dog_photo_url(breed) do
      with {:ok, photo_url, _, _} <- ApiClient.save_photo(url, name) do
        {:ok, photo_url}
      end
    end
  end

  defp fetch_dog_photo_url() do
    with {:ok, _, body} <- ApiClient.get(@dog_photo_url) do
      with %{status: "success", message: photo_url} <- body do
        {:ok, photo_url}
      end
    end
  end

  defp fetch_dog_photo_url(nil), do: fetch_dog_photo_url()

  defp fetch_dog_photo_url(breed) do
    resp = breed |> dog_photo_url |> ApiClient.get()

    with {:ok, _, body} <- resp do
      with %{status: "success", message: photo_url} <- body do
        {:ok, List.first(photo_url)}
      end
    end
  end

  defp dog_photo_url(breed), do: "https://dog.ceo/api/breed/#{breed}/images/random/1"
end
