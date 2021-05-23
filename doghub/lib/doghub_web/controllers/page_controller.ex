defmodule DoghubWeb.PageController do
  use DoghubWeb, :controller

  def index(conn, _params) do
    render(conn, "index.html")
  end
end
