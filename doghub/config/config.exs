# This file is responsible for configuring your application
# and its dependencies with the aid of the Mix.Config module.
#
# This configuration file is loaded before any dependency and
# is restricted to this project.

# General application configuration
use Mix.Config

config :doghub,
  ecto_repos: [Doghub.Repo]

# Configures the endpoint
config :doghub, DoghubWeb.Endpoint,
  url: [host: "localhost"],
  secret_key_base: "Zt5I0wMuPukOFeHlEhXxufkVT9ovsbsCPH+sfZCu9F7qanQftwRsG0ZpCU6J5U/5",
  render_errors: [view: DoghubWeb.ErrorView, accepts: ~w(html json), layout: false],
  pubsub_server: Doghub.PubSub,
  live_view: [signing_salt: "/xVcK76Q"]

# Configures Elixir's Logger
config :logger, :console,
  format: "$time $metadata[$level] $message\n",
  metadata: [:request_id]

# Use Jason for JSON parsing in Phoenix
config :phoenix, :json_library, Jason

# Import environment specific config. This must remain at the bottom
# of this file so it overrides the configuration defined above.
import_config "#{Mix.env()}.exs"
