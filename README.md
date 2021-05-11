## Oil Cleaning Service
This is an auto oil cleaning service that cleans according to the instruction provided

### Assumptions
* Cleaning instruction will only be given in the following format, any additional attribute is not taken into account.
* Attribute in coordinate format must be in the format of an integer array with a size of 2
* Navigation instructions must be given by uppercase character in N, S, E, W with no spaces separated
* Oil patches can be out of boundary but it won't be cleaned
```json
{
  "areaSize" : [5, 5],
  "startingPosition" : [1, 2],
  "oilPatches" : [
    [1, 0],
    [2, 2],
    [2, 3]
  ],
  "navigationInstructions" : "NNESEESWNWW"
}
```

### instructions
When supplied with the above defined input json, the below defined output json will be returned when called with http get
```${host}:${port}/clean```

```json
{
  "finalPosition" : [1, 3],
  "oilPatchesCleaned" : 1
}
```