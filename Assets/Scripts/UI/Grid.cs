using System;
using UnityEngine;

namespace UI {
  public class Grid : MonoBehaviour {
    private const int GridWidth = 15;
    private const int GridHeight = 11;
    private float _cellWidth, _cellHeight;
    private readonly Node[,] _grid = new Node[GridWidth, GridHeight];

    private void Start() {
      _cellWidth = Screen.width / (float)GridWidth;
      _cellHeight = Screen.height / (float)GridHeight;

      for (var x = 0; x < GridWidth; x++) {
        for (var y = 0; y < GridHeight; y++) {
          _grid[x, y] = new Node(new Vector2(x * _cellWidth + (_cellWidth / 2), y * _cellHeight + (_cellHeight / 2)), x,
            y);
        }
      }
    }

    private void OnDrawGizmos() {
      for (var x = 0; x < GridWidth; x++) {
        for (var y = 0; y < GridHeight; y++) {
          Gizmos.DrawWireCube(_grid[x, y].GetWorldPos(), new Vector2(_cellWidth, _cellHeight));
        }
      }
    }
  }
}