defmodule DoghubWeb.Router do
  use DoghubWeb, :router

  pipeline :browser do
    plug :accepts, ["html"]
    plug :fetch_session
    plug :fetch_flash
    plug :protect_from_forgery
    plug :put_secure_browser_headers
  end

  pipeline :api do
    plug :accepts, ["json"]
  end

  scope "/", DoghubWeb do
    pipe_through :browser

    get "/", PageController, :index
    get "/result", PageController, :result
  end

  scope "/api", DoghubWeb do
    pipe_through :api

    get("/profile/", ProfileController, :get)
  end
end
