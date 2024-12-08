# Short description of the milestone #

The distance \(d\) is calculated using [Haversine formula](https://en.wikipedia.org/wiki/Haversine_formula):

\[
d = 2r \cdot \arcsin\left(\sqrt{\frac{1 - \cos(\Delta\phi) + \cos(\phi_1) \cdot \cos(\phi_2) \cdot (1 - \cos(\Delta\lambda))}{2}}\right)
\]

Where:
- \(r\): Earth’s radius (approx. 6371 km)
- \(\phi_1, \phi_2\): Latitudes of the two points (in radians)
- \(\lambda_1, \lambda_2\): Longitudes of the two points (in radians)
- \(\Delta\phi\): \phi_2 - \phi_1
- \(\Delta\lambda\): \lambda_2 - \lambda_1