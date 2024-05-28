using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace UI {
  public class Node {
    private Vector2 _worldPos;
    private int _x, _y;

    public Node(Vector2 worldPos, int x, int y) {
      _worldPos = worldPos;
      _x = x;
      _y = y;
    }

    public Vector2 GetWorldPos() {
      return _worldPos;
    }
  }
}