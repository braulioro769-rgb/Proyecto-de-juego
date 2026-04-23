extends Control


func _on_volume_pressed() -> void:
	pass # Replace with function body.


func _on_salir_pressed() -> void:
	get_tree().quit()

func _on_natras_pressed() -> void:
		get_tree().change_scene_to_file("res://menu.tscn")
