defmodule Telemetry.Reading.ElectricityMeter do
  @moduledoc false
  use Protobuf, syntax: :proto3

  @type t :: %__MODULE__{
          reading: integer,
          type: String.t()
        }

  defstruct [:reading, :type]

  field :reading, 1, type: :int32
  field :type, 2, type: :string
end

defmodule Telemetry.Reading.TemperatureMeter do
  @moduledoc false
  use Protobuf, syntax: :proto3

  @type t :: %__MODULE__{
          reading: float | :infinity | :negative_infinity | :nan
        }

  defstruct [:reading]

  field :reading, 1, type: :float
end

defmodule Telemetry.Reading.WaterMeter do
  @moduledoc false
  use Protobuf, syntax: :proto3

  @type t :: %__MODULE__{
          reading: integer
        }

  defstruct [:reading]

  field :reading, 1, type: :int32
end

defmodule Telemetry.Reading do
  @moduledoc false
  use Protobuf, syntax: :proto3

  @type t :: %__MODULE__{
          meter: {atom, any},
          meter_id: String.t(),
          sender_id: String.t(),
          timestamp: String.t()
        }

  defstruct [:meter, :meter_id, :sender_id, :timestamp]

  oneof :meter, 0
  field :water, 5, type: Telemetry.Reading.WaterMeter, oneof: 0
  field :electricity, 6, type: Telemetry.Reading.ElectricityMeter, oneof: 0
  field :temperature, 7, type: Telemetry.Reading.TemperatureMeter, oneof: 0
  field :meter_id, 1, type: :string
  field :sender_id, 2, type: :string
  field :timestamp, 3, type: :string
end

defmodule Telemetry.Readings do
  @moduledoc false
  use Protobuf, syntax: :proto3

  @type t :: %__MODULE__{
          readings: [Telemetry.Reading.t()]
        }

  defstruct [:readings]

  field :readings, 1, repeated: true, type: Telemetry.Reading
end

defmodule Telemetry.Response do
  @moduledoc false
  use Protobuf, syntax: :proto3
  @type t :: %__MODULE__{}

  defstruct []
end

defmodule Telemetry.Telemetry.Service do
  @moduledoc false
  use GRPC.Service, name: "telemetry.Telemetry"

  rpc :sendReading, Telemetry.Reading, Telemetry.Response

  rpc :sendReadings, Telemetry.Readings, Telemetry.Response
end

defmodule Telemetry.Telemetry.Stub do
  @moduledoc false
  use GRPC.Stub, service: Telemetry.Telemetry.Service
end
