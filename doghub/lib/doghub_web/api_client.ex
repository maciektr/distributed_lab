defmodule DoghubWeb.ApiClient do
  @photos_root "assets/static/images/"
  @photos_url_root "/images/"

  def get(url, headers \\ []) do
    url
    |> HTTPoison.get(headers)
    |> case do
      {:ok, %{body: raw, status_code: code}} -> {code, raw}
      {:error, %{reason: reason}} -> {:error, reason}
    end
    |> (fn {code, body} ->
          body
          |> Poison.decode(keys: :atoms)
          |> case do
            {:ok, parsed} -> {:ok, code, parsed}
            _ -> {:error, body}
          end
        end).()
  end

  def save_photo(url, name \\ nil, overwrite \\ false, headers \\ []) do
    filename = url |> String.split("/") |> List.last()

    filename =
      case name do
        nil -> filename
        _ -> name <> "." <> (filename |> String.split(".") |> List.last())
      end

    path = @photos_root <> filename
    photo_url = DoghubWeb.Endpoint.url() <> @photos_url_root <> filename
    disk_path = File.cwd!() <> "/" <> path

    if overwrite or not File.exists?(path) do
      with %HTTPoison.Response{body: body} <- HTTPoison.get!(url, headers) do
        with :ok <- File.write!(path, body) do
          resize_photo(path)
          {:ok, photo_url, disk_path, url}
        end
      end
    else
      {:ok, photo_url, disk_path, nil}
    end
  end

  def resize_photo(path, width \\ 460, height \\ 460, opts \\ []) do
    Mogrify.open(path)
    |> Mogrify.resize(~s(#{width}x#{height}))
    |> Mogrify.save(opts)
  end
end
