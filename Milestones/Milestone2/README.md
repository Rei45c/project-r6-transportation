# Short description of the milestone #

The distance \(d\) is calculated using [Haversine formula](https://en.wikipedia.org/wiki/Haversine_formula):

d = 2r * arcsin(sqrt( (1 - cos(Δφ) + cos(φ1) * cos(φ2) * (1 - cos(Δλ))) / 2 ))

Where:
- r: Earth's radius (approx. 6371 km)
- φ1, φ2: Latitudes of the two points (in radians)
- λ1, λ2: Longitudes of the two points (in radians)
- Δφ: φ2 - φ1
- Δλ: λ2 - λ1