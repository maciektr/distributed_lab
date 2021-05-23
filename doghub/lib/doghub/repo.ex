defmodule Doghub.Repo do
  use Ecto.Repo,
    otp_app: :doghub,
    adapter: Ecto.Adapters.Postgres
end
