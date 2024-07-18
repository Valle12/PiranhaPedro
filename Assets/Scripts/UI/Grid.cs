using System;
using System.Numerics;
using UnityEngine;
using Vector2 = UnityEngine.Vector2;

namespace UI {
  public class Grid : MonoBehaviour {
    private const int GridWidth = 15;
    private const int GridHeight = 11;
    private float _cellWidth, _cellHeight, _cellLength;
    private readonly Node[,] _grid = new Node[GridWidth, GridHeight];

    private void Start() {
      _cellWidth = Screen.width / (float)GridWidth;
      _cellHeight = Screen.height / (float)GridHeight;
      Debug.Log("Width: " + _cellWidth + ", Height: " + _cellHeight);

      Vector2 worldBottomLeft = new Vector2(transform.position.x, transform.position.y) -
                                Vector2.right * (Screen.width / 2f) -
                                Vector2.up * (Screen.height / 2f);

      for (var x = 0; x < GridWidth; x++) {
        for (var y = 0; y < GridHeight; y++) {
          Vector2 position = worldBottomLeft + Vector2.right * (x * _cellWidth + (_cellWidth / 2)) +
                             Vector2.up * (y * _cellHeight + (_cellHeight / 2));
          _grid[x, y] = new Node(position / 1000,
            x,
            y);
        }
      }
    }

    private void OnDrawGizmos() {
      if (_grid == null || _grid[0, 0] == null) return;

      for (var x = 0; x < GridWidth; x++) {
        for (var y = 0; y < GridHeight; y++) {
          Gizmos.DrawWireCube(_grid[x, y].GetWorldPos(), new Vector2(_cellWidth, _cellHeight));
        }
      }
    }
  }
}