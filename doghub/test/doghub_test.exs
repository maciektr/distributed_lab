defmodule DoghubTest do
  use ExUnit.Case
  doctest Doghub

  test "greets the world" do
    assert Doghub.hello() == :world
  end
end
